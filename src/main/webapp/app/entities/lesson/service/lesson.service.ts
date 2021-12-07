import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILesson, getLessonIdentifier } from '../lesson.model';

export type EntityResponseType = HttpResponse<ILesson>;
export type EntityArrayResponseType = HttpResponse<ILesson[]>;

@Injectable({ providedIn: 'root' })
export class LessonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lessons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lesson: ILesson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lesson);
    return this.http
      .post<ILesson>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lesson: ILesson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lesson);
    return this.http
      .put<ILesson>(`${this.resourceUrl}/${getLessonIdentifier(lesson) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lesson: ILesson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lesson);
    return this.http
      .patch<ILesson>(`${this.resourceUrl}/${getLessonIdentifier(lesson) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILesson>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    //(courseName === null || courseName === undefined) ? courseName="" : courseName;
    return this.http
      .get<ILesson[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryWithParam(courseId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILesson[]>(this.resourceUrl+'/ByCourseId/'+courseId.toString(), { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLessonToCollectionIfMissing(lessonCollection: ILesson[], ...lessonsToCheck: (ILesson | null | undefined)[]): ILesson[] {
    const lessons: ILesson[] = lessonsToCheck.filter(isPresent);
    if (lessons.length > 0) {
      const lessonCollectionIdentifiers = lessonCollection.map(lessonItem => getLessonIdentifier(lessonItem)!);
      const lessonsToAdd = lessons.filter(lessonItem => {
        const lessonIdentifier = getLessonIdentifier(lessonItem);
        if (lessonIdentifier == null || lessonCollectionIdentifiers.includes(lessonIdentifier)) {
          return false;
        }
        lessonCollectionIdentifiers.push(lessonIdentifier);
        return true;
      });
      return [...lessonsToAdd, ...lessonCollection];
    }
    return lessonCollection;
  }

  protected convertDateFromClient(lesson: ILesson): ILesson {
    return Object.assign({}, lesson, {
      dateStart: lesson.dateStart?.isValid() ? lesson.dateStart.format(DATE_FORMAT) : undefined,
      dateEnd: lesson.dateEnd?.isValid() ? lesson.dateEnd.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateStart = res.body.dateStart ? dayjs(res.body.dateStart) : undefined;
      res.body.dateEnd = res.body.dateEnd ? dayjs(res.body.dateEnd) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((lesson: ILesson) => {
        lesson.dateStart = lesson.dateStart ? dayjs(lesson.dateStart) : undefined;
        lesson.dateEnd = lesson.dateEnd ? dayjs(lesson.dateEnd) : undefined;
      });
    }
    return res;
  }
}

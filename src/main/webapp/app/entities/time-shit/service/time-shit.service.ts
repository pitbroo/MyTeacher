import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITimeShit, getTimeShitIdentifier } from '../time-shit.model';

export type EntityResponseType = HttpResponse<ITimeShit>;
export type EntityArrayResponseType = HttpResponse<ITimeShit[]>;

@Injectable({ providedIn: 'root' })
export class TimeShitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/time-shits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(timeShit: ITimeShit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timeShit);
    return this.http
      .post<ITimeShit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(timeShit: ITimeShit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timeShit);
    return this.http
      .put<ITimeShit>(`${this.resourceUrl}/${getTimeShitIdentifier(timeShit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(timeShit: ITimeShit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timeShit);
    return this.http
      .patch<ITimeShit>(`${this.resourceUrl}/${getTimeShitIdentifier(timeShit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITimeShit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITimeShit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTimeShitToCollectionIfMissing(timeShitCollection: ITimeShit[], ...timeShitsToCheck: (ITimeShit | null | undefined)[]): ITimeShit[] {
    const timeShits: ITimeShit[] = timeShitsToCheck.filter(isPresent);
    if (timeShits.length > 0) {
      const timeShitCollectionIdentifiers = timeShitCollection.map(timeShitItem => getTimeShitIdentifier(timeShitItem)!);
      const timeShitsToAdd = timeShits.filter(timeShitItem => {
        const timeShitIdentifier = getTimeShitIdentifier(timeShitItem);
        if (timeShitIdentifier == null || timeShitCollectionIdentifiers.includes(timeShitIdentifier)) {
          return false;
        }
        timeShitCollectionIdentifiers.push(timeShitIdentifier);
        return true;
      });
      return [...timeShitsToAdd, ...timeShitCollection];
    }
    return timeShitCollection;
  }

  protected convertDateFromClient(timeShit: ITimeShit): ITimeShit {
    return Object.assign({}, timeShit, {
      date: timeShit.date?.isValid() ? timeShit.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((timeShit: ITimeShit) => {
        timeShit.date = timeShit.date ? dayjs(timeShit.date) : undefined;
      });
    }
    return res;
  }
}

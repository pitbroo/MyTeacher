import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITaskSolved, getTaskSolvedIdentifier } from '../task-solved.model';

export type EntityResponseType = HttpResponse<ITaskSolved>;
export type EntityArrayResponseType = HttpResponse<ITaskSolved[]>;

@Injectable({ providedIn: 'root' })
export class TaskSolvedService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/task-solveds');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(taskSolved: ITaskSolved): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taskSolved);
    return this.http
      .post<ITaskSolved>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(taskSolved: ITaskSolved): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taskSolved);
    return this.http
      .put<ITaskSolved>(`${this.resourceUrl}/${getTaskSolvedIdentifier(taskSolved) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(taskSolved: ITaskSolved): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taskSolved);
    return this.http
      .patch<ITaskSolved>(`${this.resourceUrl}/${getTaskSolvedIdentifier(taskSolved) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITaskSolved>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(id: any, req?: any): Observable<EntityArrayResponseType> {
    let param: string;
    id !== undefined ? param = id.toString() : param ="";
    const options = createRequestOption(req);
    return this.http
      .get<ITaskSolved[]>(this.resourceUrl+"?taskId.equals="+param, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTaskSolvedToCollectionIfMissing(
    taskSolvedCollection: ITaskSolved[],
    ...taskSolvedsToCheck: (ITaskSolved | null | undefined)[]
  ): ITaskSolved[] {
    const taskSolveds: ITaskSolved[] = taskSolvedsToCheck.filter(isPresent);
    if (taskSolveds.length > 0) {
      const taskSolvedCollectionIdentifiers = taskSolvedCollection.map(taskSolvedItem => getTaskSolvedIdentifier(taskSolvedItem)!);
      const taskSolvedsToAdd = taskSolveds.filter(taskSolvedItem => {
        const taskSolvedIdentifier = getTaskSolvedIdentifier(taskSolvedItem);
        if (taskSolvedIdentifier == null || taskSolvedCollectionIdentifiers.includes(taskSolvedIdentifier)) {
          return false;
        }
        taskSolvedCollectionIdentifiers.push(taskSolvedIdentifier);
        return true;
      });
      return [...taskSolvedsToAdd, ...taskSolvedCollection];
    }
    return taskSolvedCollection;
  }

  protected convertDateFromClient(taskSolved: ITaskSolved): ITaskSolved {
    return Object.assign({}, taskSolved, {
      deadline: taskSolved.deadline?.isValid() ? taskSolved.deadline.format(DATE_FORMAT) : undefined,
      sendDay: taskSolved.sendDay?.isValid() ? taskSolved.sendDay.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.deadline = res.body.deadline ? dayjs(res.body.deadline) : undefined;
      res.body.sendDay = res.body.sendDay ? dayjs(res.body.sendDay) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((taskSolved: ITaskSolved) => {
        taskSolved.deadline = taskSolved.deadline ? dayjs(taskSolved.deadline) : undefined;
        taskSolved.sendDay = taskSolved.sendDay ? dayjs(taskSolved.sendDay) : undefined;
      });
    }
    return res;
  }
}

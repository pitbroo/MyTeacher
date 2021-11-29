import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPoint, getPointIdentifier } from '../point.model';

export type EntityResponseType = HttpResponse<IPoint>;
export type EntityArrayResponseType = HttpResponse<IPoint[]>;

@Injectable({ providedIn: 'root' })
export class PointService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/points');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(point: IPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(point);
    return this.http
      .post<IPoint>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(point: IPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(point);
    return this.http
      .put<IPoint>(`${this.resourceUrl}/${getPointIdentifier(point) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(point: IPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(point);
    return this.http
      .patch<IPoint>(`${this.resourceUrl}/${getPointIdentifier(point) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPoint[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPointToCollectionIfMissing(pointCollection: IPoint[], ...pointsToCheck: (IPoint | null | undefined)[]): IPoint[] {
    const points: IPoint[] = pointsToCheck.filter(isPresent);
    if (points.length > 0) {
      const pointCollectionIdentifiers = pointCollection.map(pointItem => getPointIdentifier(pointItem)!);
      const pointsToAdd = points.filter(pointItem => {
        const pointIdentifier = getPointIdentifier(pointItem);
        if (pointIdentifier == null || pointCollectionIdentifiers.includes(pointIdentifier)) {
          return false;
        }
        pointCollectionIdentifiers.push(pointIdentifier);
        return true;
      });
      return [...pointsToAdd, ...pointCollection];
    }
    return pointCollection;
  }

  protected convertDateFromClient(point: IPoint): IPoint {
    return Object.assign({}, point, {
      date: point.date?.isValid() ? point.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((point: IPoint) => {
        point.date = point.date ? dayjs(point.date) : undefined;
      });
    }
    return res;
  }
}

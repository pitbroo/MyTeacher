import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmailNotification, getEmailNotificationIdentifier } from '../email-notification.model';

export type EntityResponseType = HttpResponse<IEmailNotification>;
export type EntityArrayResponseType = HttpResponse<IEmailNotification[]>;

@Injectable({ providedIn: 'root' })
export class EmailNotificationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/email-notifications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(emailNotification: IEmailNotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailNotification);
    return this.http
      .post<IEmailNotification>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(emailNotification: IEmailNotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailNotification);
    return this.http
      .put<IEmailNotification>(`${this.resourceUrl}/${getEmailNotificationIdentifier(emailNotification) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(emailNotification: IEmailNotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailNotification);
    return this.http
      .patch<IEmailNotification>(`${this.resourceUrl}/${getEmailNotificationIdentifier(emailNotification) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmailNotification>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmailNotification[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEmailNotificationToCollectionIfMissing(
    emailNotificationCollection: IEmailNotification[],
    ...emailNotificationsToCheck: (IEmailNotification | null | undefined)[]
  ): IEmailNotification[] {
    const emailNotifications: IEmailNotification[] = emailNotificationsToCheck.filter(isPresent);
    if (emailNotifications.length > 0) {
      const emailNotificationCollectionIdentifiers = emailNotificationCollection.map(
        emailNotificationItem => getEmailNotificationIdentifier(emailNotificationItem)!
      );
      const emailNotificationsToAdd = emailNotifications.filter(emailNotificationItem => {
        const emailNotificationIdentifier = getEmailNotificationIdentifier(emailNotificationItem);
        if (emailNotificationIdentifier == null || emailNotificationCollectionIdentifiers.includes(emailNotificationIdentifier)) {
          return false;
        }
        emailNotificationCollectionIdentifiers.push(emailNotificationIdentifier);
        return true;
      });
      return [...emailNotificationsToAdd, ...emailNotificationCollection];
    }
    return emailNotificationCollection;
  }

  protected convertDateFromClient(emailNotification: IEmailNotification): IEmailNotification {
    return Object.assign({}, emailNotification, {
      time: emailNotification.time?.isValid() ? emailNotification.time.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.time = res.body.time ? dayjs(res.body.time) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((emailNotification: IEmailNotification) => {
        emailNotification.time = emailNotification.time ? dayjs(emailNotification.time) : undefined;
      });
    }
    return res;
  }
}

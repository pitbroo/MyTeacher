import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmailNotificationUser, getEmailNotificationUserIdentifier } from '../email-notification-user.model';

export type EntityResponseType = HttpResponse<IEmailNotificationUser>;
export type EntityArrayResponseType = HttpResponse<IEmailNotificationUser[]>;

@Injectable({ providedIn: 'root' })
export class EmailNotificationUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/email-notification-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(emailNotificationUser: IEmailNotificationUser): Observable<EntityResponseType> {
    return this.http.post<IEmailNotificationUser>(this.resourceUrl, emailNotificationUser, { observe: 'response' });
  }

  update(emailNotificationUser: IEmailNotificationUser): Observable<EntityResponseType> {
    return this.http.put<IEmailNotificationUser>(
      `${this.resourceUrl}/${getEmailNotificationUserIdentifier(emailNotificationUser) as number}`,
      emailNotificationUser,
      { observe: 'response' }
    );
  }

  partialUpdate(emailNotificationUser: IEmailNotificationUser): Observable<EntityResponseType> {
    return this.http.patch<IEmailNotificationUser>(
      `${this.resourceUrl}/${getEmailNotificationUserIdentifier(emailNotificationUser) as number}`,
      emailNotificationUser,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmailNotificationUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmailNotificationUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEmailNotificationUserToCollectionIfMissing(
    emailNotificationUserCollection: IEmailNotificationUser[],
    ...emailNotificationUsersToCheck: (IEmailNotificationUser | null | undefined)[]
  ): IEmailNotificationUser[] {
    const emailNotificationUsers: IEmailNotificationUser[] = emailNotificationUsersToCheck.filter(isPresent);
    if (emailNotificationUsers.length > 0) {
      const emailNotificationUserCollectionIdentifiers = emailNotificationUserCollection.map(
        emailNotificationUserItem => getEmailNotificationUserIdentifier(emailNotificationUserItem)!
      );
      const emailNotificationUsersToAdd = emailNotificationUsers.filter(emailNotificationUserItem => {
        const emailNotificationUserIdentifier = getEmailNotificationUserIdentifier(emailNotificationUserItem);
        if (
          emailNotificationUserIdentifier == null ||
          emailNotificationUserCollectionIdentifiers.includes(emailNotificationUserIdentifier)
        ) {
          return false;
        }
        emailNotificationUserCollectionIdentifiers.push(emailNotificationUserIdentifier);
        return true;
      });
      return [...emailNotificationUsersToAdd, ...emailNotificationUserCollection];
    }
    return emailNotificationUserCollection;
  }
}

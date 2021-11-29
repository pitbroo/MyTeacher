import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaymentUser, getPaymentUserIdentifier } from '../payment-user.model';

export type EntityResponseType = HttpResponse<IPaymentUser>;
export type EntityArrayResponseType = HttpResponse<IPaymentUser[]>;

@Injectable({ providedIn: 'root' })
export class PaymentUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paymentUser: IPaymentUser): Observable<EntityResponseType> {
    return this.http.post<IPaymentUser>(this.resourceUrl, paymentUser, { observe: 'response' });
  }

  update(paymentUser: IPaymentUser): Observable<EntityResponseType> {
    return this.http.put<IPaymentUser>(`${this.resourceUrl}/${getPaymentUserIdentifier(paymentUser) as number}`, paymentUser, {
      observe: 'response',
    });
  }

  partialUpdate(paymentUser: IPaymentUser): Observable<EntityResponseType> {
    return this.http.patch<IPaymentUser>(`${this.resourceUrl}/${getPaymentUserIdentifier(paymentUser) as number}`, paymentUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPaymentUserToCollectionIfMissing(
    paymentUserCollection: IPaymentUser[],
    ...paymentUsersToCheck: (IPaymentUser | null | undefined)[]
  ): IPaymentUser[] {
    const paymentUsers: IPaymentUser[] = paymentUsersToCheck.filter(isPresent);
    if (paymentUsers.length > 0) {
      const paymentUserCollectionIdentifiers = paymentUserCollection.map(paymentUserItem => getPaymentUserIdentifier(paymentUserItem)!);
      const paymentUsersToAdd = paymentUsers.filter(paymentUserItem => {
        const paymentUserIdentifier = getPaymentUserIdentifier(paymentUserItem);
        if (paymentUserIdentifier == null || paymentUserCollectionIdentifiers.includes(paymentUserIdentifier)) {
          return false;
        }
        paymentUserCollectionIdentifiers.push(paymentUserIdentifier);
        return true;
      });
      return [...paymentUsersToAdd, ...paymentUserCollection];
    }
    return paymentUserCollection;
  }
}

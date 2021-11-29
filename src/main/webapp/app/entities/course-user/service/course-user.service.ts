import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourseUser, getCourseUserIdentifier } from '../course-user.model';

export type EntityResponseType = HttpResponse<ICourseUser>;
export type EntityArrayResponseType = HttpResponse<ICourseUser[]>;

@Injectable({ providedIn: 'root' })
export class CourseUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/course-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(courseUser: ICourseUser): Observable<EntityResponseType> {
    return this.http.post<ICourseUser>(this.resourceUrl, courseUser, { observe: 'response' });
  }

  update(courseUser: ICourseUser): Observable<EntityResponseType> {
    return this.http.put<ICourseUser>(`${this.resourceUrl}/${getCourseUserIdentifier(courseUser) as number}`, courseUser, {
      observe: 'response',
    });
  }

  partialUpdate(courseUser: ICourseUser): Observable<EntityResponseType> {
    return this.http.patch<ICourseUser>(`${this.resourceUrl}/${getCourseUserIdentifier(courseUser) as number}`, courseUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourseUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourseUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCourseUserToCollectionIfMissing(
    courseUserCollection: ICourseUser[],
    ...courseUsersToCheck: (ICourseUser | null | undefined)[]
  ): ICourseUser[] {
    const courseUsers: ICourseUser[] = courseUsersToCheck.filter(isPresent);
    if (courseUsers.length > 0) {
      const courseUserCollectionIdentifiers = courseUserCollection.map(courseUserItem => getCourseUserIdentifier(courseUserItem)!);
      const courseUsersToAdd = courseUsers.filter(courseUserItem => {
        const courseUserIdentifier = getCourseUserIdentifier(courseUserItem);
        if (courseUserIdentifier == null || courseUserCollectionIdentifiers.includes(courseUserIdentifier)) {
          return false;
        }
        courseUserCollectionIdentifiers.push(courseUserIdentifier);
        return true;
      });
      return [...courseUsersToAdd, ...courseUserCollection];
    }
    return courseUserCollection;
  }
}

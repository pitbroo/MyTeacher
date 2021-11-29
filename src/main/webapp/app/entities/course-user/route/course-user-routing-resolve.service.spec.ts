jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICourseUser, CourseUser } from '../course-user.model';
import { CourseUserService } from '../service/course-user.service';

import { CourseUserRoutingResolveService } from './course-user-routing-resolve.service';

describe('Service Tests', () => {
  describe('CourseUser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CourseUserRoutingResolveService;
    let service: CourseUserService;
    let resultCourseUser: ICourseUser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CourseUserRoutingResolveService);
      service = TestBed.inject(CourseUserService);
      resultCourseUser = undefined;
    });

    describe('resolve', () => {
      it('should return ICourseUser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseUser).toEqual({ id: 123 });
      });

      it('should return new ICourseUser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseUser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCourseUser).toEqual(new CourseUser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CourseUser })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseUser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

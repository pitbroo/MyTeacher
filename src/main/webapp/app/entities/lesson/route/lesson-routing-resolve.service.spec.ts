jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILesson, Lesson } from '../lesson.model';
import { LessonService } from '../service/lesson.service';

import { LessonRoutingResolveService } from './lesson-routing-resolve.service';

describe('Service Tests', () => {
  describe('Lesson routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LessonRoutingResolveService;
    let service: LessonService;
    let resultLesson: ILesson | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LessonRoutingResolveService);
      service = TestBed.inject(LessonService);
      resultLesson = undefined;
    });

    describe('resolve', () => {
      it('should return ILesson returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLesson = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLesson).toEqual({ id: 123 });
      });

      it('should return new ILesson if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLesson = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLesson).toEqual(new Lesson());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Lesson })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLesson = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLesson).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

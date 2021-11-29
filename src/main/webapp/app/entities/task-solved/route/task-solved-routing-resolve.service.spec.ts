jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITaskSolved, TaskSolved } from '../task-solved.model';
import { TaskSolvedService } from '../service/task-solved.service';

import { TaskSolvedRoutingResolveService } from './task-solved-routing-resolve.service';

describe('Service Tests', () => {
  describe('TaskSolved routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TaskSolvedRoutingResolveService;
    let service: TaskSolvedService;
    let resultTaskSolved: ITaskSolved | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TaskSolvedRoutingResolveService);
      service = TestBed.inject(TaskSolvedService);
      resultTaskSolved = undefined;
    });

    describe('resolve', () => {
      it('should return ITaskSolved returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTaskSolved = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTaskSolved).toEqual({ id: 123 });
      });

      it('should return new ITaskSolved if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTaskSolved = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTaskSolved).toEqual(new TaskSolved());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TaskSolved })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTaskSolved = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTaskSolved).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

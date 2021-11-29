jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITimeShit, TimeShit } from '../time-shit.model';
import { TimeShitService } from '../service/time-shit.service';

import { TimeShitRoutingResolveService } from './time-shit-routing-resolve.service';

describe('Service Tests', () => {
  describe('TimeShit routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TimeShitRoutingResolveService;
    let service: TimeShitService;
    let resultTimeShit: ITimeShit | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TimeShitRoutingResolveService);
      service = TestBed.inject(TimeShitService);
      resultTimeShit = undefined;
    });

    describe('resolve', () => {
      it('should return ITimeShit returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTimeShit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTimeShit).toEqual({ id: 123 });
      });

      it('should return new ITimeShit if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTimeShit = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTimeShit).toEqual(new TimeShit());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TimeShit })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTimeShit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTimeShit).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

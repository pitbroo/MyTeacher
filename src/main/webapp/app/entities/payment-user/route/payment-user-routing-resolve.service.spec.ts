jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPaymentUser, PaymentUser } from '../payment-user.model';
import { PaymentUserService } from '../service/payment-user.service';

import { PaymentUserRoutingResolveService } from './payment-user-routing-resolve.service';

describe('Service Tests', () => {
  describe('PaymentUser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PaymentUserRoutingResolveService;
    let service: PaymentUserService;
    let resultPaymentUser: IPaymentUser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PaymentUserRoutingResolveService);
      service = TestBed.inject(PaymentUserService);
      resultPaymentUser = undefined;
    });

    describe('resolve', () => {
      it('should return IPaymentUser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPaymentUser).toEqual({ id: 123 });
      });

      it('should return new IPaymentUser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentUser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPaymentUser).toEqual(new PaymentUser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PaymentUser })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPaymentUser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

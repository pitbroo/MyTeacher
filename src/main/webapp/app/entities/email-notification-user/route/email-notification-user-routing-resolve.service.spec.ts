jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEmailNotificationUser, EmailNotificationUser } from '../email-notification-user.model';
import { EmailNotificationUserService } from '../service/email-notification-user.service';

import { EmailNotificationUserRoutingResolveService } from './email-notification-user-routing-resolve.service';

describe('Service Tests', () => {
  describe('EmailNotificationUser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EmailNotificationUserRoutingResolveService;
    let service: EmailNotificationUserService;
    let resultEmailNotificationUser: IEmailNotificationUser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EmailNotificationUserRoutingResolveService);
      service = TestBed.inject(EmailNotificationUserService);
      resultEmailNotificationUser = undefined;
    });

    describe('resolve', () => {
      it('should return IEmailNotificationUser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEmailNotificationUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEmailNotificationUser).toEqual({ id: 123 });
      });

      it('should return new IEmailNotificationUser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEmailNotificationUser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEmailNotificationUser).toEqual(new EmailNotificationUser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EmailNotificationUser })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEmailNotificationUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEmailNotificationUser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

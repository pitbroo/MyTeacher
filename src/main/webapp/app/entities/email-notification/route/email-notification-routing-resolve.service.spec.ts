jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEmailNotification, EmailNotification } from '../email-notification.model';
import { EmailNotificationService } from '../service/email-notification.service';

import { EmailNotificationRoutingResolveService } from './email-notification-routing-resolve.service';

describe('Service Tests', () => {
  describe('EmailNotification routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EmailNotificationRoutingResolveService;
    let service: EmailNotificationService;
    let resultEmailNotification: IEmailNotification | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EmailNotificationRoutingResolveService);
      service = TestBed.inject(EmailNotificationService);
      resultEmailNotification = undefined;
    });

    describe('resolve', () => {
      it('should return IEmailNotification returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEmailNotification = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEmailNotification).toEqual({ id: 123 });
      });

      it('should return new IEmailNotification if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEmailNotification = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEmailNotification).toEqual(new EmailNotification());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EmailNotification })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEmailNotification = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEmailNotification).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

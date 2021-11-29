jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EmailNotificationUserService } from '../service/email-notification-user.service';
import { IEmailNotificationUser, EmailNotificationUser } from '../email-notification-user.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmailNotification } from 'app/entities/email-notification/email-notification.model';
import { EmailNotificationService } from 'app/entities/email-notification/service/email-notification.service';

import { EmailNotificationUserUpdateComponent } from './email-notification-user-update.component';

describe('Component Tests', () => {
  describe('EmailNotificationUser Management Update Component', () => {
    let comp: EmailNotificationUserUpdateComponent;
    let fixture: ComponentFixture<EmailNotificationUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let emailNotificationUserService: EmailNotificationUserService;
    let userService: UserService;
    let emailNotificationService: EmailNotificationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmailNotificationUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EmailNotificationUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmailNotificationUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      emailNotificationUserService = TestBed.inject(EmailNotificationUserService);
      userService = TestBed.inject(UserService);
      emailNotificationService = TestBed.inject(EmailNotificationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const emailNotificationUser: IEmailNotificationUser = { id: 456 };
        const user: IUser = { id: 13717 };
        emailNotificationUser.user = user;

        const userCollection: IUser[] = [{ id: 72920 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ emailNotificationUser });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call EmailNotification query and add missing value', () => {
        const emailNotificationUser: IEmailNotificationUser = { id: 456 };
        const emailNotification: IEmailNotification = { id: 64382 };
        emailNotificationUser.emailNotification = emailNotification;

        const emailNotificationCollection: IEmailNotification[] = [{ id: 71121 }];
        jest.spyOn(emailNotificationService, 'query').mockReturnValue(of(new HttpResponse({ body: emailNotificationCollection })));
        const additionalEmailNotifications = [emailNotification];
        const expectedCollection: IEmailNotification[] = [...additionalEmailNotifications, ...emailNotificationCollection];
        jest.spyOn(emailNotificationService, 'addEmailNotificationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ emailNotificationUser });
        comp.ngOnInit();

        expect(emailNotificationService.query).toHaveBeenCalled();
        expect(emailNotificationService.addEmailNotificationToCollectionIfMissing).toHaveBeenCalledWith(
          emailNotificationCollection,
          ...additionalEmailNotifications
        );
        expect(comp.emailNotificationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const emailNotificationUser: IEmailNotificationUser = { id: 456 };
        const user: IUser = { id: 59947 };
        emailNotificationUser.user = user;
        const emailNotification: IEmailNotification = { id: 63479 };
        emailNotificationUser.emailNotification = emailNotification;

        activatedRoute.data = of({ emailNotificationUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(emailNotificationUser));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.emailNotificationsSharedCollection).toContain(emailNotification);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EmailNotificationUser>>();
        const emailNotificationUser = { id: 123 };
        jest.spyOn(emailNotificationUserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ emailNotificationUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: emailNotificationUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(emailNotificationUserService.update).toHaveBeenCalledWith(emailNotificationUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EmailNotificationUser>>();
        const emailNotificationUser = new EmailNotificationUser();
        jest.spyOn(emailNotificationUserService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ emailNotificationUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: emailNotificationUser }));
        saveSubject.complete();

        // THEN
        expect(emailNotificationUserService.create).toHaveBeenCalledWith(emailNotificationUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EmailNotificationUser>>();
        const emailNotificationUser = { id: 123 };
        jest.spyOn(emailNotificationUserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ emailNotificationUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(emailNotificationUserService.update).toHaveBeenCalledWith(emailNotificationUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEmailNotificationById', () => {
        it('Should return tracked EmailNotification primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEmailNotificationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

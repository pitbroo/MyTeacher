jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EmailNotificationService } from '../service/email-notification.service';
import { IEmailNotification, EmailNotification } from '../email-notification.model';

import { EmailNotificationUpdateComponent } from './email-notification-update.component';

describe('Component Tests', () => {
  describe('EmailNotification Management Update Component', () => {
    let comp: EmailNotificationUpdateComponent;
    let fixture: ComponentFixture<EmailNotificationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let emailNotificationService: EmailNotificationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmailNotificationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EmailNotificationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmailNotificationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      emailNotificationService = TestBed.inject(EmailNotificationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const emailNotification: IEmailNotification = { id: 456 };

        activatedRoute.data = of({ emailNotification });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(emailNotification));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EmailNotification>>();
        const emailNotification = { id: 123 };
        jest.spyOn(emailNotificationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ emailNotification });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: emailNotification }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(emailNotificationService.update).toHaveBeenCalledWith(emailNotification);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EmailNotification>>();
        const emailNotification = new EmailNotification();
        jest.spyOn(emailNotificationService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ emailNotification });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: emailNotification }));
        saveSubject.complete();

        // THEN
        expect(emailNotificationService.create).toHaveBeenCalledWith(emailNotification);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EmailNotification>>();
        const emailNotification = { id: 123 };
        jest.spyOn(emailNotificationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ emailNotification });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(emailNotificationService.update).toHaveBeenCalledWith(emailNotification);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

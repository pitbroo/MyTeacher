jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EmailNotificationUserService } from '../service/email-notification-user.service';

import { EmailNotificationUserDeleteDialogComponent } from './email-notification-user-delete-dialog.component';

describe('Component Tests', () => {
  describe('EmailNotificationUser Management Delete Component', () => {
    let comp: EmailNotificationUserDeleteDialogComponent;
    let fixture: ComponentFixture<EmailNotificationUserDeleteDialogComponent>;
    let service: EmailNotificationUserService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmailNotificationUserDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(EmailNotificationUserDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmailNotificationUserDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EmailNotificationUserService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});

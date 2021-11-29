import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EmailNotificationService } from '../service/email-notification.service';

import { EmailNotificationComponent } from './email-notification.component';

describe('Component Tests', () => {
  describe('EmailNotification Management Component', () => {
    let comp: EmailNotificationComponent;
    let fixture: ComponentFixture<EmailNotificationComponent>;
    let service: EmailNotificationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmailNotificationComponent],
      })
        .overrideTemplate(EmailNotificationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmailNotificationComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EmailNotificationService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.emailNotifications?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

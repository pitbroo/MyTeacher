import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EmailNotificationUserService } from '../service/email-notification-user.service';

import { EmailNotificationUserComponent } from './email-notification-user.component';

describe('Component Tests', () => {
  describe('EmailNotificationUser Management Component', () => {
    let comp: EmailNotificationUserComponent;
    let fixture: ComponentFixture<EmailNotificationUserComponent>;
    let service: EmailNotificationUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmailNotificationUserComponent],
      })
        .overrideTemplate(EmailNotificationUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmailNotificationUserComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EmailNotificationUserService);

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
      expect(comp.emailNotificationUsers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

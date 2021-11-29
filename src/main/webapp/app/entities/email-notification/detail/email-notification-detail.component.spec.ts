import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmailNotificationDetailComponent } from './email-notification-detail.component';

describe('Component Tests', () => {
  describe('EmailNotification Management Detail Component', () => {
    let comp: EmailNotificationDetailComponent;
    let fixture: ComponentFixture<EmailNotificationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EmailNotificationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ emailNotification: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EmailNotificationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmailNotificationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load emailNotification on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.emailNotification).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

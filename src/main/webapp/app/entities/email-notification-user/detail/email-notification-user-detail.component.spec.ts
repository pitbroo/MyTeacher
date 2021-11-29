import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmailNotificationUserDetailComponent } from './email-notification-user-detail.component';

describe('Component Tests', () => {
  describe('EmailNotificationUser Management Detail Component', () => {
    let comp: EmailNotificationUserDetailComponent;
    let fixture: ComponentFixture<EmailNotificationUserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EmailNotificationUserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ emailNotificationUser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EmailNotificationUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmailNotificationUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load emailNotificationUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.emailNotificationUser).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

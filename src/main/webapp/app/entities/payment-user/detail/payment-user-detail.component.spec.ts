import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaymentUserDetailComponent } from './payment-user-detail.component';

describe('Component Tests', () => {
  describe('PaymentUser Management Detail Component', () => {
    let comp: PaymentUserDetailComponent;
    let fixture: ComponentFixture<PaymentUserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PaymentUserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ paymentUser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PaymentUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentUser).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

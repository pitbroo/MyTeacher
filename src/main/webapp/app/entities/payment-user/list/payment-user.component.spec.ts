import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PaymentUserService } from '../service/payment-user.service';

import { PaymentUserComponent } from './payment-user.component';

describe('Component Tests', () => {
  describe('PaymentUser Management Component', () => {
    let comp: PaymentUserComponent;
    let fixture: ComponentFixture<PaymentUserComponent>;
    let service: PaymentUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentUserComponent],
      })
        .overrideTemplate(PaymentUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentUserComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PaymentUserService);

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
      expect(comp.paymentUsers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

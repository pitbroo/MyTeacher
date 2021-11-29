import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PointService } from '../service/point.service';

import { PointComponent } from './point.component';

describe('Component Tests', () => {
  describe('Point Management Component', () => {
    let comp: PointComponent;
    let fixture: ComponentFixture<PointComponent>;
    let service: PointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointComponent],
      })
        .overrideTemplate(PointComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PointComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PointService);

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
      expect(comp.points?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

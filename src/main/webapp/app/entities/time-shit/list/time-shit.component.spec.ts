import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TimeShitService } from '../service/time-shit.service';

import { TimeShitComponent } from './time-shit.component';

describe('Component Tests', () => {
  describe('TimeShit Management Component', () => {
    let comp: TimeShitComponent;
    let fixture: ComponentFixture<TimeShitComponent>;
    let service: TimeShitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TimeShitComponent],
      })
        .overrideTemplate(TimeShitComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TimeShitComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TimeShitService);

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
      expect(comp.timeShits?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

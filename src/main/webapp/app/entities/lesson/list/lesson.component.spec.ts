import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LessonService } from '../service/lesson.service';

import { LessonComponent } from './lesson.component';

describe('Component Tests', () => {
  describe('Lesson Management Component', () => {
    let comp: LessonComponent;
    let fixture: ComponentFixture<LessonComponent>;
    let service: LessonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonComponent],
      })
        .overrideTemplate(LessonComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LessonService);

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
      expect(comp.lessons?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

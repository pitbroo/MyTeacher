import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LessonDetailComponent } from './lesson-detail.component';

describe('Component Tests', () => {
  describe('Lesson Management Detail Component', () => {
    let comp: LessonDetailComponent;
    let fixture: ComponentFixture<LessonDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LessonDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lesson: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LessonDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LessonDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lesson on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lesson).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

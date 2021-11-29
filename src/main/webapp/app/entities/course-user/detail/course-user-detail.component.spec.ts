import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseUserDetailComponent } from './course-user-detail.component';

describe('Component Tests', () => {
  describe('CourseUser Management Detail Component', () => {
    let comp: CourseUserDetailComponent;
    let fixture: ComponentFixture<CourseUserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CourseUserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ courseUser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CourseUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courseUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseUser).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

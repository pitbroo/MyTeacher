import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CourseUserService } from '../service/course-user.service';

import { CourseUserComponent } from './course-user.component';

describe('Component Tests', () => {
  describe('CourseUser Management Component', () => {
    let comp: CourseUserComponent;
    let fixture: ComponentFixture<CourseUserComponent>;
    let service: CourseUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourseUserComponent],
      })
        .overrideTemplate(CourseUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseUserComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CourseUserService);

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
      expect(comp.courseUsers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LessonService } from '../service/lesson.service';
import { ILesson, Lesson } from '../lesson.model';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

import { LessonUpdateComponent } from './lesson-update.component';

describe('Component Tests', () => {
  describe('Lesson Management Update Component', () => {
    let comp: LessonUpdateComponent;
    let fixture: ComponentFixture<LessonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lessonService: LessonService;
    let courseService: CourseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LessonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lessonService = TestBed.inject(LessonService);
      courseService = TestBed.inject(CourseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Course query and add missing value', () => {
        const lesson: ILesson = { id: 456 };
        const course: ICourse = { id: 82893 };
        lesson.course = course;

        const courseCollection: ICourse[] = [{ id: 6588 }];
        jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
        const additionalCourses = [course];
        const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
        jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lesson });
        comp.ngOnInit();

        expect(courseService.query).toHaveBeenCalled();
        expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, ...additionalCourses);
        expect(comp.coursesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lesson: ILesson = { id: 456 };
        const course: ICourse = { id: 35331 };
        lesson.course = course;

        activatedRoute.data = of({ lesson });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lesson));
        expect(comp.coursesSharedCollection).toContain(course);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Lesson>>();
        const lesson = { id: 123 };
        jest.spyOn(lessonService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lesson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lesson }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lessonService.update).toHaveBeenCalledWith(lesson);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Lesson>>();
        const lesson = new Lesson();
        jest.spyOn(lessonService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lesson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lesson }));
        saveSubject.complete();

        // THEN
        expect(lessonService.create).toHaveBeenCalledWith(lesson);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Lesson>>();
        const lesson = { id: 123 };
        jest.spyOn(lessonService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lesson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lessonService.update).toHaveBeenCalledWith(lesson);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCourseById', () => {
        it('Should return tracked Course primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCourseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

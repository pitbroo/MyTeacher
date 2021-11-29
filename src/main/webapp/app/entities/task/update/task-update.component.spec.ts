jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TaskService } from '../service/task.service';
import { ITask, Task } from '../task.model';
import { ILesson } from 'app/entities/lesson/lesson.model';
import { LessonService } from 'app/entities/lesson/service/lesson.service';

import { TaskUpdateComponent } from './task-update.component';

describe('Component Tests', () => {
  describe('Task Management Update Component', () => {
    let comp: TaskUpdateComponent;
    let fixture: ComponentFixture<TaskUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let taskService: TaskService;
    let lessonService: LessonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TaskUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TaskUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      taskService = TestBed.inject(TaskService);
      lessonService = TestBed.inject(LessonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Lesson query and add missing value', () => {
        const task: ITask = { id: 456 };
        const lesson: ILesson = { id: 91587 };
        task.lesson = lesson;

        const lessonCollection: ILesson[] = [{ id: 46256 }];
        jest.spyOn(lessonService, 'query').mockReturnValue(of(new HttpResponse({ body: lessonCollection })));
        const additionalLessons = [lesson];
        const expectedCollection: ILesson[] = [...additionalLessons, ...lessonCollection];
        jest.spyOn(lessonService, 'addLessonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ task });
        comp.ngOnInit();

        expect(lessonService.query).toHaveBeenCalled();
        expect(lessonService.addLessonToCollectionIfMissing).toHaveBeenCalledWith(lessonCollection, ...additionalLessons);
        expect(comp.lessonsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const task: ITask = { id: 456 };
        const lesson: ILesson = { id: 32163 };
        task.lesson = lesson;

        activatedRoute.data = of({ task });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(task));
        expect(comp.lessonsSharedCollection).toContain(lesson);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Task>>();
        const task = { id: 123 };
        jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ task });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: task }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(taskService.update).toHaveBeenCalledWith(task);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Task>>();
        const task = new Task();
        jest.spyOn(taskService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ task });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: task }));
        saveSubject.complete();

        // THEN
        expect(taskService.create).toHaveBeenCalledWith(task);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Task>>();
        const task = { id: 123 };
        jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ task });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(taskService.update).toHaveBeenCalledWith(task);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLessonById', () => {
        it('Should return tracked Lesson primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLessonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TaskSolvedService } from '../service/task-solved.service';
import { ITaskSolved, TaskSolved } from '../task-solved.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITask } from 'app/entities/task/task.model';
import { TaskService } from 'app/entities/task/service/task.service';

import { TaskSolvedUpdateComponent } from './task-solved-update.component';

describe('Component Tests', () => {
  describe('TaskSolved Management Update Component', () => {
    let comp: TaskSolvedUpdateComponent;
    let fixture: ComponentFixture<TaskSolvedUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let taskSolvedService: TaskSolvedService;
    let userService: UserService;
    let taskService: TaskService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TaskSolvedUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TaskSolvedUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskSolvedUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      taskSolvedService = TestBed.inject(TaskSolvedService);
      userService = TestBed.inject(UserService);
      taskService = TestBed.inject(TaskService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const taskSolved: ITaskSolved = { id: 456 };
        const user: IUser = { id: 40555 };
        taskSolved.user = user;

        const userCollection: IUser[] = [{ id: 64979 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ taskSolved });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Task query and add missing value', () => {
        const taskSolved: ITaskSolved = { id: 456 };
        const task: ITask = { id: 89379 };
        taskSolved.task = task;

        const taskCollection: ITask[] = [{ id: 53725 }];
        jest.spyOn(taskService, 'query').mockReturnValue(of(new HttpResponse({ body: taskCollection })));
        const additionalTasks = [task];
        const expectedCollection: ITask[] = [...additionalTasks, ...taskCollection];
        jest.spyOn(taskService, 'addTaskToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ taskSolved });
        comp.ngOnInit();

        expect(taskService.query).toHaveBeenCalled();
        expect(taskService.addTaskToCollectionIfMissing).toHaveBeenCalledWith(taskCollection, ...additionalTasks);
        expect(comp.tasksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const taskSolved: ITaskSolved = { id: 456 };
        const user: IUser = { id: 14704 };
        taskSolved.user = user;
        const task: ITask = { id: 4822 };
        taskSolved.task = task;

        activatedRoute.data = of({ taskSolved });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(taskSolved));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.tasksSharedCollection).toContain(task);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TaskSolved>>();
        const taskSolved = { id: 123 };
        jest.spyOn(taskSolvedService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taskSolved });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: taskSolved }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(taskSolvedService.update).toHaveBeenCalledWith(taskSolved);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TaskSolved>>();
        const taskSolved = new TaskSolved();
        jest.spyOn(taskSolvedService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taskSolved });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: taskSolved }));
        saveSubject.complete();

        // THEN
        expect(taskSolvedService.create).toHaveBeenCalledWith(taskSolved);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TaskSolved>>();
        const taskSolved = { id: 123 };
        jest.spyOn(taskSolvedService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taskSolved });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(taskSolvedService.update).toHaveBeenCalledWith(taskSolved);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTaskById', () => {
        it('Should return tracked Task primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTaskById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

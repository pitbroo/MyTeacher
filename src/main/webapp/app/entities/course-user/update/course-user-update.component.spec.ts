jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourseUserService } from '../service/course-user.service';
import { ICourseUser, CourseUser } from '../course-user.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

import { CourseUserUpdateComponent } from './course-user-update.component';

describe('Component Tests', () => {
  describe('CourseUser Management Update Component', () => {
    let comp: CourseUserUpdateComponent;
    let fixture: ComponentFixture<CourseUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courseUserService: CourseUserService;
    let userService: UserService;
    let courseService: CourseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourseUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourseUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courseUserService = TestBed.inject(CourseUserService);
      userService = TestBed.inject(UserService);
      courseService = TestBed.inject(CourseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const courseUser: ICourseUser = { id: 456 };
        const user: IUser = { id: 66807 };
        courseUser.user = user;

        const userCollection: IUser[] = [{ id: 38626 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ courseUser });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Course query and add missing value', () => {
        const courseUser: ICourseUser = { id: 456 };
        const course: ICourse = { id: 42743 };
        courseUser.course = course;

        const courseCollection: ICourse[] = [{ id: 28715 }];
        jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
        const additionalCourses = [course];
        const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
        jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ courseUser });
        comp.ngOnInit();

        expect(courseService.query).toHaveBeenCalled();
        expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, ...additionalCourses);
        expect(comp.coursesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const courseUser: ICourseUser = { id: 456 };
        const user: IUser = { id: 5357 };
        courseUser.user = user;
        const course: ICourse = { id: 40085 };
        courseUser.course = course;

        activatedRoute.data = of({ courseUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(courseUser));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.coursesSharedCollection).toContain(course);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CourseUser>>();
        const courseUser = { id: 123 };
        jest.spyOn(courseUserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courseUserService.update).toHaveBeenCalledWith(courseUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CourseUser>>();
        const courseUser = new CourseUser();
        jest.spyOn(courseUserService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseUser }));
        saveSubject.complete();

        // THEN
        expect(courseUserService.create).toHaveBeenCalledWith(courseUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CourseUser>>();
        const courseUser = { id: 123 };
        jest.spyOn(courseUserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courseUserService.update).toHaveBeenCalledWith(courseUser);
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

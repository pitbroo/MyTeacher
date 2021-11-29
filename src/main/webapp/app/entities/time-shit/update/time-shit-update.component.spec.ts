jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TimeShitService } from '../service/time-shit.service';
import { ITimeShit, TimeShit } from '../time-shit.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TimeShitUpdateComponent } from './time-shit-update.component';

describe('Component Tests', () => {
  describe('TimeShit Management Update Component', () => {
    let comp: TimeShitUpdateComponent;
    let fixture: ComponentFixture<TimeShitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let timeShitService: TimeShitService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TimeShitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TimeShitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TimeShitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      timeShitService = TestBed.inject(TimeShitService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const timeShit: ITimeShit = { id: 456 };
        const user: IUser = { id: 89357 };
        timeShit.user = user;

        const userCollection: IUser[] = [{ id: 30436 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ timeShit });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const timeShit: ITimeShit = { id: 456 };
        const user: IUser = { id: 42756 };
        timeShit.user = user;

        activatedRoute.data = of({ timeShit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(timeShit));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TimeShit>>();
        const timeShit = { id: 123 };
        jest.spyOn(timeShitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ timeShit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: timeShit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(timeShitService.update).toHaveBeenCalledWith(timeShit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TimeShit>>();
        const timeShit = new TimeShit();
        jest.spyOn(timeShitService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ timeShit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: timeShit }));
        saveSubject.complete();

        // THEN
        expect(timeShitService.create).toHaveBeenCalledWith(timeShit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TimeShit>>();
        const timeShit = { id: 123 };
        jest.spyOn(timeShitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ timeShit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(timeShitService.update).toHaveBeenCalledWith(timeShit);
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
    });
  });
});

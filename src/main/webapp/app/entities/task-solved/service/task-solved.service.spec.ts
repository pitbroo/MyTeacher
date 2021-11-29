import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITaskSolved, TaskSolved } from '../task-solved.model';

import { TaskSolvedService } from './task-solved.service';

describe('Service Tests', () => {
  describe('TaskSolved Service', () => {
    let service: TaskSolvedService;
    let httpMock: HttpTestingController;
    let elemDefault: ITaskSolved;
    let expectedResult: ITaskSolved | ITaskSolved[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TaskSolvedService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        pointGrade: 0,
        content: 'AAAAAAA',
        deadline: currentDate,
        sendDay: currentDate,
        answer: 'AAAAAAA',
        attachmentContentType: 'image/png',
        attachment: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            deadline: currentDate.format(DATE_FORMAT),
            sendDay: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TaskSolved', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            deadline: currentDate.format(DATE_FORMAT),
            sendDay: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            deadline: currentDate,
            sendDay: currentDate,
          },
          returnedFromService
        );

        service.create(new TaskSolved()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TaskSolved', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pointGrade: 1,
            content: 'BBBBBB',
            deadline: currentDate.format(DATE_FORMAT),
            sendDay: currentDate.format(DATE_FORMAT),
            answer: 'BBBBBB',
            attachment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            deadline: currentDate,
            sendDay: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TaskSolved', () => {
        const patchObject = Object.assign(
          {
            pointGrade: 1,
            content: 'BBBBBB',
            deadline: currentDate.format(DATE_FORMAT),
            attachment: 'BBBBBB',
          },
          new TaskSolved()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            deadline: currentDate,
            sendDay: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TaskSolved', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pointGrade: 1,
            content: 'BBBBBB',
            deadline: currentDate.format(DATE_FORMAT),
            sendDay: currentDate.format(DATE_FORMAT),
            answer: 'BBBBBB',
            attachment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            deadline: currentDate,
            sendDay: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TaskSolved', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTaskSolvedToCollectionIfMissing', () => {
        it('should add a TaskSolved to an empty array', () => {
          const taskSolved: ITaskSolved = { id: 123 };
          expectedResult = service.addTaskSolvedToCollectionIfMissing([], taskSolved);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(taskSolved);
        });

        it('should not add a TaskSolved to an array that contains it', () => {
          const taskSolved: ITaskSolved = { id: 123 };
          const taskSolvedCollection: ITaskSolved[] = [
            {
              ...taskSolved,
            },
            { id: 456 },
          ];
          expectedResult = service.addTaskSolvedToCollectionIfMissing(taskSolvedCollection, taskSolved);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TaskSolved to an array that doesn't contain it", () => {
          const taskSolved: ITaskSolved = { id: 123 };
          const taskSolvedCollection: ITaskSolved[] = [{ id: 456 }];
          expectedResult = service.addTaskSolvedToCollectionIfMissing(taskSolvedCollection, taskSolved);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(taskSolved);
        });

        it('should add only unique TaskSolved to an array', () => {
          const taskSolvedArray: ITaskSolved[] = [{ id: 123 }, { id: 456 }, { id: 63338 }];
          const taskSolvedCollection: ITaskSolved[] = [{ id: 123 }];
          expectedResult = service.addTaskSolvedToCollectionIfMissing(taskSolvedCollection, ...taskSolvedArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const taskSolved: ITaskSolved = { id: 123 };
          const taskSolved2: ITaskSolved = { id: 456 };
          expectedResult = service.addTaskSolvedToCollectionIfMissing([], taskSolved, taskSolved2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(taskSolved);
          expect(expectedResult).toContain(taskSolved2);
        });

        it('should accept null and undefined values', () => {
          const taskSolved: ITaskSolved = { id: 123 };
          expectedResult = service.addTaskSolvedToCollectionIfMissing([], null, taskSolved, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(taskSolved);
        });

        it('should return initial array if no TaskSolved is added', () => {
          const taskSolvedCollection: ITaskSolved[] = [{ id: 123 }];
          expectedResult = service.addTaskSolvedToCollectionIfMissing(taskSolvedCollection, undefined, null);
          expect(expectedResult).toEqual(taskSolvedCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

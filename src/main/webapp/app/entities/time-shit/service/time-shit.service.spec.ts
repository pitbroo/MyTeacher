import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITimeShit, TimeShit } from '../time-shit.model';

import { TimeShitService } from './time-shit.service';

describe('Service Tests', () => {
  describe('TimeShit Service', () => {
    let service: TimeShitService;
    let httpMock: HttpTestingController;
    let elemDefault: ITimeShit;
    let expectedResult: ITimeShit | ITimeShit[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TimeShitService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        present: false,
        date: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TimeShit', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new TimeShit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TimeShit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            present: true,
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TimeShit', () => {
        const patchObject = Object.assign(
          {
            present: true,
            date: currentDate.format(DATE_FORMAT),
          },
          new TimeShit()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TimeShit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            present: true,
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TimeShit', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTimeShitToCollectionIfMissing', () => {
        it('should add a TimeShit to an empty array', () => {
          const timeShit: ITimeShit = { id: 123 };
          expectedResult = service.addTimeShitToCollectionIfMissing([], timeShit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(timeShit);
        });

        it('should not add a TimeShit to an array that contains it', () => {
          const timeShit: ITimeShit = { id: 123 };
          const timeShitCollection: ITimeShit[] = [
            {
              ...timeShit,
            },
            { id: 456 },
          ];
          expectedResult = service.addTimeShitToCollectionIfMissing(timeShitCollection, timeShit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TimeShit to an array that doesn't contain it", () => {
          const timeShit: ITimeShit = { id: 123 };
          const timeShitCollection: ITimeShit[] = [{ id: 456 }];
          expectedResult = service.addTimeShitToCollectionIfMissing(timeShitCollection, timeShit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(timeShit);
        });

        it('should add only unique TimeShit to an array', () => {
          const timeShitArray: ITimeShit[] = [{ id: 123 }, { id: 456 }, { id: 51037 }];
          const timeShitCollection: ITimeShit[] = [{ id: 123 }];
          expectedResult = service.addTimeShitToCollectionIfMissing(timeShitCollection, ...timeShitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const timeShit: ITimeShit = { id: 123 };
          const timeShit2: ITimeShit = { id: 456 };
          expectedResult = service.addTimeShitToCollectionIfMissing([], timeShit, timeShit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(timeShit);
          expect(expectedResult).toContain(timeShit2);
        });

        it('should accept null and undefined values', () => {
          const timeShit: ITimeShit = { id: 123 };
          expectedResult = service.addTimeShitToCollectionIfMissing([], null, timeShit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(timeShit);
        });

        it('should return initial array if no TimeShit is added', () => {
          const timeShitCollection: ITimeShit[] = [{ id: 123 }];
          expectedResult = service.addTimeShitToCollectionIfMissing(timeShitCollection, undefined, null);
          expect(expectedResult).toEqual(timeShitCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRanking, Ranking } from '../ranking.model';

import { RankingService } from './ranking.service';

describe('Service Tests', () => {
  describe('Ranking Service', () => {
    let service: RankingService;
    let httpMock: HttpTestingController;
    let elemDefault: IRanking;
    let expectedResult: IRanking | IRanking[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RankingService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        points: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Ranking', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Ranking()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Ranking', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            points: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Ranking', () => {
        const patchObject = Object.assign({}, new Ranking());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Ranking', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            points: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Ranking', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRankingToCollectionIfMissing', () => {
        it('should add a Ranking to an empty array', () => {
          const ranking: IRanking = { id: 123 };
          expectedResult = service.addRankingToCollectionIfMissing([], ranking);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ranking);
        });

        it('should not add a Ranking to an array that contains it', () => {
          const ranking: IRanking = { id: 123 };
          const rankingCollection: IRanking[] = [
            {
              ...ranking,
            },
            { id: 456 },
          ];
          expectedResult = service.addRankingToCollectionIfMissing(rankingCollection, ranking);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Ranking to an array that doesn't contain it", () => {
          const ranking: IRanking = { id: 123 };
          const rankingCollection: IRanking[] = [{ id: 456 }];
          expectedResult = service.addRankingToCollectionIfMissing(rankingCollection, ranking);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ranking);
        });

        it('should add only unique Ranking to an array', () => {
          const rankingArray: IRanking[] = [{ id: 123 }, { id: 456 }, { id: 80903 }];
          const rankingCollection: IRanking[] = [{ id: 123 }];
          expectedResult = service.addRankingToCollectionIfMissing(rankingCollection, ...rankingArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ranking: IRanking = { id: 123 };
          const ranking2: IRanking = { id: 456 };
          expectedResult = service.addRankingToCollectionIfMissing([], ranking, ranking2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ranking);
          expect(expectedResult).toContain(ranking2);
        });

        it('should accept null and undefined values', () => {
          const ranking: IRanking = { id: 123 };
          expectedResult = service.addRankingToCollectionIfMissing([], null, ranking, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ranking);
        });

        it('should return initial array if no Ranking is added', () => {
          const rankingCollection: IRanking[] = [{ id: 123 }];
          expectedResult = service.addRankingToCollectionIfMissing(rankingCollection, undefined, null);
          expect(expectedResult).toEqual(rankingCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

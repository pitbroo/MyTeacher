import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRanking, getRankingIdentifier } from '../ranking.model';

export type EntityResponseType = HttpResponse<IRanking>;
export type EntityArrayResponseType = HttpResponse<IRanking[]>;

@Injectable({ providedIn: 'root' })
export class RankingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rankings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ranking: IRanking): Observable<EntityResponseType> {
    return this.http.post<IRanking>(this.resourceUrl, ranking, { observe: 'response' });
  }

  update(ranking: IRanking): Observable<EntityResponseType> {
    return this.http.put<IRanking>(`${this.resourceUrl}/${getRankingIdentifier(ranking) as number}`, ranking, { observe: 'response' });
  }

  partialUpdate(ranking: IRanking): Observable<EntityResponseType> {
    return this.http.patch<IRanking>(`${this.resourceUrl}/${getRankingIdentifier(ranking) as number}`, ranking, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRanking>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRanking[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRankingToCollectionIfMissing(rankingCollection: IRanking[], ...rankingsToCheck: (IRanking | null | undefined)[]): IRanking[] {
    const rankings: IRanking[] = rankingsToCheck.filter(isPresent);
    if (rankings.length > 0) {
      const rankingCollectionIdentifiers = rankingCollection.map(rankingItem => getRankingIdentifier(rankingItem)!);
      const rankingsToAdd = rankings.filter(rankingItem => {
        const rankingIdentifier = getRankingIdentifier(rankingItem);
        if (rankingIdentifier == null || rankingCollectionIdentifiers.includes(rankingIdentifier)) {
          return false;
        }
        rankingCollectionIdentifiers.push(rankingIdentifier);
        return true;
      });
      return [...rankingsToAdd, ...rankingCollection];
    }
    return rankingCollection;
  }
}

export interface IRanking {
  id?: number;
  points?: number | null;
  nowa?: string | null;
}

export class Ranking implements IRanking {
  constructor(public id?: number, public points?: number | null, public nowa?: string | null) {}
}

export function getRankingIdentifier(ranking: IRanking): number | undefined {
  return ranking.id;
}

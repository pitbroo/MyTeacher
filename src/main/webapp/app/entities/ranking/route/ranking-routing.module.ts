import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RankingComponent } from '../list/ranking.component';
import { RankingDetailComponent } from '../detail/ranking-detail.component';
import { RankingUpdateComponent } from '../update/ranking-update.component';
import { RankingRoutingResolveService } from './ranking-routing-resolve.service';

const rankingRoute: Routes = [
  {
    path: '',
    component: RankingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RankingDetailComponent,
    resolve: {
      ranking: RankingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RankingUpdateComponent,
    resolve: {
      ranking: RankingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RankingUpdateComponent,
    resolve: {
      ranking: RankingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rankingRoute)],
  exports: [RouterModule],
})
export class RankingRoutingModule {}

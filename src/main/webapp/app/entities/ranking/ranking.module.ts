import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RankingComponent } from './list/ranking.component';
import { RankingDetailComponent } from './detail/ranking-detail.component';
import { RankingUpdateComponent } from './update/ranking-update.component';
import { RankingDeleteDialogComponent } from './delete/ranking-delete-dialog.component';
import { RankingRoutingModule } from './route/ranking-routing.module';

@NgModule({
  imports: [SharedModule, RankingRoutingModule],
  declarations: [RankingComponent, RankingDetailComponent, RankingUpdateComponent, RankingDeleteDialogComponent],
  entryComponents: [RankingDeleteDialogComponent],
})
export class RankingModule {}

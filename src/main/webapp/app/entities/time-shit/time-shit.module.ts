import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TimeShitComponent } from './list/time-shit.component';
import { TimeShitDetailComponent } from './detail/time-shit-detail.component';
import { TimeShitUpdateComponent } from './update/time-shit-update.component';
import { TimeShitDeleteDialogComponent } from './delete/time-shit-delete-dialog.component';
import { TimeShitRoutingModule } from './route/time-shit-routing.module';

@NgModule({
  imports: [SharedModule, TimeShitRoutingModule],
  declarations: [TimeShitComponent, TimeShitDetailComponent, TimeShitUpdateComponent, TimeShitDeleteDialogComponent],
  entryComponents: [TimeShitDeleteDialogComponent],
})
export class TimeShitModule {}

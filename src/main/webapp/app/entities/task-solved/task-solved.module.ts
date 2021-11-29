import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TaskSolvedComponent } from './list/task-solved.component';
import { TaskSolvedDetailComponent } from './detail/task-solved-detail.component';
import { TaskSolvedUpdateComponent } from './update/task-solved-update.component';
import { TaskSolvedDeleteDialogComponent } from './delete/task-solved-delete-dialog.component';
import { TaskSolvedRoutingModule } from './route/task-solved-routing.module';

@NgModule({
  imports: [SharedModule, TaskSolvedRoutingModule],
  declarations: [TaskSolvedComponent, TaskSolvedDetailComponent, TaskSolvedUpdateComponent, TaskSolvedDeleteDialogComponent],
  entryComponents: [TaskSolvedDeleteDialogComponent],
})
export class TaskSolvedModule {}

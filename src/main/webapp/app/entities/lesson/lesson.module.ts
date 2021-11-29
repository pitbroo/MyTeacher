import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LessonComponent } from './list/lesson.component';
import { LessonDetailComponent } from './detail/lesson-detail.component';
import { LessonUpdateComponent } from './update/lesson-update.component';
import { LessonDeleteDialogComponent } from './delete/lesson-delete-dialog.component';
import { LessonRoutingModule } from './route/lesson-routing.module';

@NgModule({
  imports: [SharedModule, LessonRoutingModule],
  declarations: [LessonComponent, LessonDetailComponent, LessonUpdateComponent, LessonDeleteDialogComponent],
  entryComponents: [LessonDeleteDialogComponent],
})
export class LessonModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CourseUserComponent } from './list/course-user.component';
import { CourseUserDetailComponent } from './detail/course-user-detail.component';
import { CourseUserUpdateComponent } from './update/course-user-update.component';
import { CourseUserDeleteDialogComponent } from './delete/course-user-delete-dialog.component';
import { CourseUserRoutingModule } from './route/course-user-routing.module';

@NgModule({
  imports: [SharedModule, CourseUserRoutingModule],
  declarations: [CourseUserComponent, CourseUserDetailComponent, CourseUserUpdateComponent, CourseUserDeleteDialogComponent],
  entryComponents: [CourseUserDeleteDialogComponent],
})
export class CourseUserModule {}

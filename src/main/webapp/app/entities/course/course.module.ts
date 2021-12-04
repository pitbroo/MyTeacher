import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CourseComponent } from './list/course.component';
import { CourseDetailComponent } from './detail/course-detail.component';
import { CourseUpdateComponent } from './update/course-update.component';
import { CourseDeleteDialogComponent } from './delete/course-delete-dialog.component';
import { CourseRoutingModule } from './route/course-routing.module';
import { CourseBuyComponentComponent } from './course-buy-component/course-buy-component.component';

@NgModule({
  imports: [SharedModule, CourseRoutingModule],
  declarations: [CourseComponent, CourseDetailComponent, CourseUpdateComponent, CourseDeleteDialogComponent, CourseBuyComponentComponent],
  entryComponents: [CourseDeleteDialogComponent],
})
export class CourseModule {}

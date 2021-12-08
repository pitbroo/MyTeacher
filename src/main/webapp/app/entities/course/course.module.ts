import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CourseComponent } from './list/course.component';
import { CourseDetailComponent } from './detail/course-detail.component';
import { CourseUpdateComponent } from './update/course-update.component';
import { CourseDeleteDialogComponent } from './delete/course-delete-dialog.component';
import { CourseRoutingModule } from './route/course-routing.module';
import { CourseBuyComponentComponent } from './course-buy-component/course-buy-component.component';
import {LessonModule} from "../lesson/lesson.module";

@NgModule({
    imports: [SharedModule, CourseRoutingModule, LessonModule],
    declarations: [CourseComponent, CourseDetailComponent, CourseUpdateComponent, CourseDeleteDialogComponent, CourseBuyComponentComponent],
    entryComponents: [CourseDeleteDialogComponent],
    exports: [
        CourseComponent
    ]
})
export class CourseModule {}

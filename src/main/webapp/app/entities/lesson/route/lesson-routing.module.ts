import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LessonComponent } from '../list/lesson.component';
import { LessonDetailComponent } from '../detail/lesson-detail.component';
import { LessonUpdateComponent } from '../update/lesson-update.component';
import { LessonRoutingResolveService } from './lesson-routing-resolve.service';

const lessonRoute: Routes = [
  {
    path: '',
    component: LessonComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LessonDetailComponent,
    resolve: {
      lesson: LessonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LessonUpdateComponent,
    resolve: {
      lesson: LessonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LessonUpdateComponent,
    resolve: {
      lesson: LessonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lessonRoute)],
  exports: [RouterModule],
})
export class LessonRoutingModule {}

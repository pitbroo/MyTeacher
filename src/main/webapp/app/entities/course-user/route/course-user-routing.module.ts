import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourseUserComponent } from '../list/course-user.component';
import { CourseUserDetailComponent } from '../detail/course-user-detail.component';
import { CourseUserUpdateComponent } from '../update/course-user-update.component';
import { CourseUserRoutingResolveService } from './course-user-routing-resolve.service';

const courseUserRoute: Routes = [
  {
    path: '',
    component: CourseUserComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseUserDetailComponent,
    resolve: {
      courseUser: CourseUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseUserUpdateComponent,
    resolve: {
      courseUser: CourseUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseUserUpdateComponent,
    resolve: {
      courseUser: CourseUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(courseUserRoute)],
  exports: [RouterModule],
})
export class CourseUserRoutingModule {}

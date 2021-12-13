import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TaskSolvedComponent } from '../list/task-solved.component';
import { TaskSolvedDetailComponent } from '../detail/task-solved-detail.component';
import { TaskSolvedUpdateComponent } from '../update/task-solved-update.component';
import { TaskSolvedRoutingResolveService } from './task-solved-routing-resolve.service';

const taskSolvedRoute: Routes = [
  {
    path: '',
    component: TaskSolvedComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaskSolvedDetailComponent,
    resolve: {
      taskSolved: TaskSolvedRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new/:taskId',
    component: TaskSolvedUpdateComponent,
    resolve: {
      taskSolved: TaskSolvedRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {id: "sahjdbash"}
  },
  {
    path: ':id/edit',
    component: TaskSolvedUpdateComponent,
    resolve: {
      taskSolved: TaskSolvedRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(taskSolvedRoute)],
  exports: [RouterModule],
})
export class TaskSolvedRoutingModule {}

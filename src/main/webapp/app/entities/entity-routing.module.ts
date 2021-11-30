import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'course',
        data: { pageTitle: 'myTeacherApp.course.home.title' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      {
        path: 'point',
        data: { pageTitle: 'myTeacherApp.point.home.title' },
        loadChildren: () => import('./point/point.module').then(m => m.PointModule),
      },
      {
        path: 'lesson',
        data: { pageTitle: 'myTeacherApp.lesson.home.title' },
        loadChildren: () => import('./lesson/lesson.module').then(m => m.LessonModule),
      },
      {
        path: 'task',
        data: { pageTitle: 'myTeacherApp.task.home.title' },
        loadChildren: () => import('./task/task.module').then(m => m.TaskModule),
      },
      {
        path: 'task-solved',
        data: { pageTitle: 'myTeacherApp.taskSolved.home.title' },
        loadChildren: () => import('./task-solved/task-solved.module').then(m => m.TaskSolvedModule),
      },
      {
        path: 'time-shit',
        data: { pageTitle: 'myTeacherApp.timeShit.home.title' },
        loadChildren: () => import('./time-shit/time-shit.module').then(m => m.TimeShitModule),
      },
      {
        path: 'email-notification',
        data: { pageTitle: 'myTeacherApp.emailNotification.home.title' },
        loadChildren: () => import('./email-notification/email-notification.module').then(m => m.EmailNotificationModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'myTeacherApp.payment.home.title' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      {
        path: 'email-notification-user',
        data: { pageTitle: 'myTeacherApp.emailNotificationUser.home.title' },
        loadChildren: () => import('./email-notification-user/email-notification-user.module').then(m => m.EmailNotificationUserModule),
      },
      {
        path: 'course-user',
        data: { pageTitle: 'myTeacherApp.courseUser.home.title' },
        loadChildren: () => import('./course-user/course-user.module').then(m => m.CourseUserModule),
      },
      {
        path: 'payment-user',
        data: { pageTitle: 'myTeacherApp.paymentUser.home.title' },
        loadChildren: () => import('./payment-user/payment-user.module').then(m => m.PaymentUserModule),
      },
      {
        path: 'ranking',
        data: { pageTitle: 'myTeacherApp.ranking.home.title' },
        loadChildren: () => import('./ranking/ranking.module').then(m => m.RankingModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

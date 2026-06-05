import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BaseRouteFilter} from '../basic/basic-route.filter';
import {DefaultPageComponent} from './home-page/default-page.component';
import {HomeComponent} from './home.component';

const routes: Routes = [{
  path: 'home',
  component: HomeComponent,
  canActivate: [BaseRouteFilter],
  children: [
    {
      path: '',
      component: DefaultPageComponent
    }, {
      path: 'personalInfo',
      loadChildren: () => import('./personal-info/personal-info.module').then(m => m.PersonalInfoModule)
    },
    {
      path: 'authorizeManage',
      loadChildren: () => import('./authorize-management/authorize-management.module').then(m => m.AuthorizeManagementModule)
    },
    {
      path: 'basicManage',
      loadChildren: () => import('./basic-management/basic-management.module').then(m => m.BasicManagementModule)
    },
    {
      path: 'websiteManage',
      loadChildren: () => import('./website-management/website-management.module').then(m => m.WebsiteManagementModule)
    },
    {
      path: 'apiManage',
      loadChildren: () => import('./api-management/api-management.module').then(m => m.ApiManagementModule)
    },
    {
      path: 'appManage',
      loadChildren: () => import('./app-management/app-management.module').then(m => m.AppManagementModule)
    }, {
      path: 'auditManage',
      loadChildren: () => import('./audit-management/audit-management.module').then(m => m.AuditManagementModule)
    }
  ]
}];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class HomeRoutingModule {
}

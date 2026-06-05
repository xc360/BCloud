import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: 'app',
    loadChildren: () => import('./app/app.module').then(m => m.AppModule)
  },
  {
    path: 'authority',
    loadChildren: () => import('./authority/authority.module').then(m => m.AuthorityModule)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuditManagementRoutingModule {
}

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavedchecklistsComponent } from './savedchecklists.component';

describe('SavedchecklistsComponent', () => {
  let component: SavedchecklistsComponent;
  let fixture: ComponentFixture<SavedchecklistsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SavedchecklistsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SavedchecklistsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

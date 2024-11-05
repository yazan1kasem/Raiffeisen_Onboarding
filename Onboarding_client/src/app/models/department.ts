export class Department {
  id: string;
  gerät: string;
  administration: string;
  software: string;
  suchbegriff: string;

  constructor(
    id: string,
    gerät: string,
    administration: string,
    software: string,
    suchbegriff: string
  ) {
    this.id = id;
    this.gerät = gerät;
    this.administration = administration;
    this.software = software;
    this.suchbegriff = suchbegriff;
  }
}

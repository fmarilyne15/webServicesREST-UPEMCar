import { withStyles, WithStyles } from '@material-ui/core/styles';

import { ADD_TO_BASKET, BASKET_LIST, CONNEXION, GET_PURCHASES, HOME, PRICE_CONVERSION, PURCHASE } from "./appUrl";

import { BasketCustomized } from "./basket";
import { ConnexionCustomized } from "./connexion";
import { PurchasesCustomized } from "./purchases";

import AppBar from '@material-ui/core/AppBar';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import FormControl from '@material-ui/core/FormControl';
import Input from '@material-ui/core/Input';
import InputBase from '@material-ui/core/InputBase';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Paper from '@material-ui/core/Paper';
import Select from '@material-ui/core/Select';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Toolbar from '@material-ui/core/Toolbar';
import Tooltip from '@material-ui/core/Tooltip';
import Typography from '@material-ui/core/Typography';

import AddIcon from '@material-ui/icons/Add';
import CarsBought from '@material-ui/icons/LocalCarWash';
import Basket from '@material-ui/icons/ShoppingBasket';

import axios from 'axios';
import * as React from 'react';

import logoUpem from './logo-upem.png';

const decoratedAppBar = withStyles(theme => ({
    answer: {
        backgroundColor: 'red',
        color: "#FFF",
        marginRight: '30%',
    },
    app: {
        marginLeft: '10%',
        width: '80%'
    },
    avatar: {
        height: 60,
        width: 60,
    },
    basket: {
        float: 'right',
        fontSize: 60,
        marginRight: '10%',
    },
    button: {
        backgroundColor: '#EA5A0B',
        color: "#FFF",
        marginRight: '5%',
    },
    div: {
        backgroundColor: '#EA5A0B',
        height: 60,
        width: '100%',
    },
    fab: {
        backgroundColor: '#000',
    },
    formControl: {
        margin: theme.spacing.unit,
        marginLeft: '-74.5%',
        maxWidth: 300,
        minWidth: 120,
    },
    inputBase: {
        width: theme.spacing.unit * 50,
    },
    inputLabel: {
        color: '#000',
        fontSize: 20,
        width: '200%'
    },
    paper: {
        marginLeft: '10%',
        marginTop: -10,
        width: '80%'
    },
    puchase: {
        float: 'right',
        fontSize: 60,
        marginBottom: -110,
        marginLeft: '68%',
    },
    resetButton: {
        backgroundColor: 'red',
        color: "#FFF",
        marginRight: '20%',
    },
    root: {
        flexGrow: 1,
    },
    row: {
        '&:nth-of-type(odd)': {
            backgroundColor: theme.palette.background.default,
        },
    },
    select: {
        border: '1px solid #000',
        float: 'left',
        marginRight: '50%',
        width: '210%'
    },
    tableCell: {
        color: theme.palette.common.white,
        fontSize: 14,
    },
    tableHead: {
        backgroundColor: '#EA5A0B',
    },
    toolBar: {
        width: '98%'
    },
    typography: {
        backgroundColor: '#EA5A0B',
        color: "#FFF",
        fontSize: 20
    }
}));

interface IAppBar {
    readonly addToBasket: boolean;
    readonly carNumber: string;
    readonly checkCardAndCvc: boolean;
    readonly checkConnexion: boolean;
    readonly currencies: string[];
    readonly cvc: string;
    readonly delivery: string;
    readonly mark: string;
    readonly openOrCloseBasket: boolean;
    readonly price: number;
    readonly matVehicle: string;
    readonly myBasket: any[];
    readonly name: string;
    readonly openPayment: boolean;
    readonly openPurchase: boolean;
    readonly purchaseAnswer: string,
    readonly purchases: any[],
    readonly userLogin: string;
    readonly userPassword: string;
    readonly vehicleIndex: number;
    readonly vehicles: any[];
}

export const AppBarCustomized = decoratedAppBar(class extends React.Component<WithStyles<'puchase' | 'answer' | 'button' | 'inputBase' | 'inputLabel' | 'basket' | 'app' | 'toolBar' | 'div' | 'avatar' | 'fab' | 'formControl' | 'paper' | 'root' | 'row' | 'select' | 'tableHead' | 'tableCell' | 'resetButton' | 'typography'>, IAppBar>{
    constructor(props: any) {
        super(props);
        this.state = {
            addToBasket: false,
            carNumber: '',
            checkCardAndCvc: false,
            checkConnexion: false,
            currencies: [],
            cvc: '',
            delivery: '',
            mark: '',
            matVehicle: '',
            myBasket: [],
            name: 'USD',
            openOrCloseBasket: false,
            openPayment: false,
            openPurchase: false,
            price: 0,
            purchaseAnswer: '',
            purchases: [],
            userLogin: '',
            userPassword: '',
            vehicleIndex: -1,
            vehicles: []
        }

        this.home = this.home.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handlePayment = this.handlePayment.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.handleOpenOrCloseBasket = this.handleOpenOrCloseBasket.bind(this);
        this.addInMyBasket = this.addInMyBasket.bind(this);
        this.handleConfirmAddToBasket = this.handleConfirmAddToBasket.bind(this);
        this.closeCardNumberAndCvCDialog = this.closeCardNumberAndCvCDialog.bind(this);
        this.handleChangeInputCard = this.handleChangeInputCard.bind(this);
        this.handleChangeInputCvc = this.handleChangeInputCvc.bind(this);
        this.handleChangeInputLogin = this.handleChangeInputLogin.bind(this);
        this.handleChangeInputPassword = this.handleChangeInputPassword.bind(this);
        this.connexion = this.connexion.bind(this);
        this.updateListVehicle = this.updateListVehicle.bind(this);
        this.viewPurchases = this.viewPurchases.bind(this);
        this.deconnect = this.deconnect.bind(this);
    }

    public render() {

        const { classes } = this.props;
        const { vehicles } = this.state;

        return (
            <div className={classes.root}>

                {!this.state.checkConnexion ?
                    // Affiche la page principale si la connexion a réussie
                    <ConnexionCustomized login={this.handleChangeInputLogin} password={this.handleChangeInputPassword} connexion={this.connexion} />

                    :

                    <span>
                        <AppBar position="static" color="default" className={classes.app}>
                            <Toolbar className={classes.toolBar}>
                                <img alt="Logo upem" src={logoUpem} className={classes.avatar} />
                                <div className={classes.div}> 
                                    <Button onClick={this.updateListVehicle} className={classes.button}> Actualiser </Button>
                                    <Button onClick={this.deconnect} className={classes.button}> Deconnecter </Button>
                                </div>
                            </Toolbar>
                        </AppBar>

                        <div className={classes.app}>
                        {/*Affiche la liste déroulante pour sélectionner la dévise */}
                        <FormControl className={classes.formControl}>
                            <InputLabel htmlFor="select-multiple" className={classes.inputLabel}>Sélectionnez votre dévise</InputLabel>
                            <Select className={classes.select}
                                value={this.state.name}
                                onChange={this.handleChange}
                                input={<Input id="select-multiple" defaultValue="USD"/>}
                            >
                                {this.state.currencies.map(money => (
                                    <MenuItem key={money} value={money}>
                                        {money}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>

                        {/*Affiche le panier*/}
                            <CarsBought className={classes.puchase}  onClick={this.viewPurchases}/>
                            <Basket className={classes.basket} onClick={this.handleOpenOrCloseBasket(this.state.userLogin, this.state.userPassword)} />
                         </div>
                        {this.state.addToBasket ?
                            // Demande confirmation pour ajouter au panier
                            <Dialog
                                aria-labelledby="simple-modal-title"
                                aria-describedby="simple-modal-description"
                                open={this.state.addToBasket}
                                onClose={this.handleConfirmAddToBasket('')}
                            >
                                <DialogContent>
                                    Voulez-vous ajouter ce véhicule dans votre panier ?
                                </DialogContent>
                                <DialogActions>
                                    <Button onClick={this.addInMyBasket(this.state.userLogin, this.state.userPassword, this.state.matVehicle)} className={classes.button}>
                                        Oui
                                    </Button>
                                    <Button onClick={this.handleConfirmAddToBasket('')} className={classes.answer}>
                                        Non
                                    </Button>
                                </DialogActions>
                            </Dialog>

                            :

                            <span />
                        }

                        {this.state.openOrCloseBasket ?
                            // Affiche le contenu du panier
                            <BasketCustomized data={this.state.myBasket} updateListVehicle={this.updateListVehicle} currencyName={this.state.name}/>

                            :

                            this.state.openPurchase ? 
                            
                            <PurchasesCustomized data={this.state.purchases} currencyName={this.state.name}/>
                            
                            :

                            <span>
                                {this.state.checkCardAndCvc ?
                                    // Affiche le status de l'achat
                                    <Dialog
                                        aria-labelledby="simple-modal-title"
                                        aria-describedby="simple-modal-description"
                                        open={this.state.checkCardAndCvc}
                                        onClose={this.closeCardNumberAndCvCDialog}
                                    >
                                        <DialogContent>
                                            {this.state.purchaseAnswer}
                                        </DialogContent>
                                        <DialogActions>
                                            <Button onClick={this.closeCardNumberAndCvCDialog} className={classes.resetButton}>
                                                OK
                                            </Button>
                                        </DialogActions>
                                    </Dialog>

                                    :

                                    <span />
                                }


                                {this.state.openPayment ?
                                    // Fenetre pour éffectuer un achat
                                    <Dialog
                                        aria-labelledby="simple-modal-title"
                                        aria-describedby="simple-modal-description"
                                        open={this.state.openPayment}
                                        onClose={this.handleClose}
                                    >
                                        <DialogContent>
                                            <Typography className = {classes.typography}> {"Matricule : " + this.state.matVehicle }  <br/>
                                             {"Marque : " + this.state.mark } <br/> { "Livraison : " + this.state.delivery } <br/> {" Prix : " + this.state.price}</Typography> <br/>
                                            <InputBase className={classes.inputBase} placeholder="Numéro de carte crédit" id={this.state.carNumber} onChange={this.handleChangeInputCard} autoFocus={true} /> <br /> <br />
                                            <InputBase type='password' className={classes.inputBase} placeholder="Cryptogramme" id={this.state.cvc} onChange={this.handleChangeInputCvc} />
                                        </DialogContent>
                                        <DialogActions>
                                            <Button onClick={this.buyVehicle(this.state.matVehicle, this.state.vehicleIndex)} className={classes.button}>
                                                Acheter
                                            </Button>
                                            <Button onClick={this.handleClose} className={classes.resetButton}>
                                                Annuler
                                            </Button>
                                        </DialogActions>
                                    </Dialog>

                                    :

                                    <span />
                                }

                                <Paper className={classes.paper} elevation={8} square={true}>
                                    {/*Liste de véhicules*/}
                                    <Table>
                                        <TableHead className={classes.tableHead}>
                                            <TableRow >
                                                <TableCell className={classes.tableCell}>Genre</TableCell>
                                                <TableCell className={classes.tableCell}>Immatriculation</TableCell>
                                                <TableCell className={classes.tableCell}>Marque</TableCell>
                                                <TableCell className={classes.tableCell}>Carburant</TableCell>
                                                <TableCell className={classes.tableCell}>An. Circulation</TableCell>
                                                <TableCell className={classes.tableCell}>Livraison</TableCell>
                                                <TableCell className={classes.tableCell}>Prix</TableCell>
                                                <TableCell className={classes.tableCell}>Acheter</TableCell>
                                                <TableCell className={classes.tableCell}>Panier</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {vehicles.map((vehicle, index) => {
                                                return (
                                                    <TableRow className={classes.row} key={index}>
                                                        <TableCell component="th" scope="row">{vehicle.kind}</TableCell>
                                                        <TableCell >{vehicle.mat}</TableCell>
                                                        <TableCell >{vehicle.mark}</TableCell>
                                                        <TableCell >{vehicle.fuel}</TableCell>
                                                        <TableCell >{vehicle.circulated}</TableCell>                                                        <TableCell >{vehicle.delivery}</TableCell>
                                                        <TableCell >{vehicle.price}</TableCell>
                                                        <TableCell >
                                                            <Tooltip title="Acheter">
                                                                <Button variant="fab" color="primary" className={classes.fab} onClick={this.handlePayment(vehicle.mat,vehicle.mark,vehicle.delivery,vehicle.price, index)}>
                                                                    {this.state.name}
                                                                </Button>
                                                            </Tooltip>
                                                        </TableCell>
                                                        <TableCell >
                                                            <Tooltip title="Ajouter">
                                                                <Button variant="fab" color="primary" className={classes.fab} onClick={this.handleConfirmAddToBasket(vehicle.mat)}>
                                                                    <AddIcon />
                                                                </Button>
                                                            </Tooltip>
                                                        </TableCell>
                                                    </TableRow>
                                                );
                                            })
                                            }
                                        </TableBody>
                                    </Table>
                                </Paper>
                            </span>
                        }
                    </span>
                }
            </div>
        );
    }

    // Page d'acceuil
    private home(login: string, password: string) {
        if(login !== "" && password !== ""){
            axios.get(HOME).then(response => {
                const allVehicleAndCurrencies = response.data;
                let myBasketContent: any;
                const getMyBasket: string = BASKET_LIST + "?mail=" + this.state.userLogin + "&password=" + this.state.userPassword;
    
                axios.get(getMyBasket).then(res => {
                    myBasketContent = res.data;
                }).catch(error => {
                    console.log(error);
                });
    
                this.setState({ vehicles: allVehicleAndCurrencies[0],currencies:allVehicleAndCurrencies[1], myBasket: myBasketContent });
            }).catch(error => {
                console.log(error);
            });
        }
    }

    // Récupère la dévise sélectinnée
    private handleChange(event: any) {
        const currencySelevcted: string = event.target.value;
        axios.post(PRICE_CONVERSION, { currency: currencySelevcted }).then(response => {
            this.setState({ name: currencySelevcted, vehicles: response.data });
        }).catch(error => {
            console.log(error);
        });
    }

    // Récupère numéro carte de crédit
    private handleChangeInputCard(event: any) {
        this.setState({ carNumber: event.target.value });
    }

    // Récupère le cryptogramme
    private handleChangeInputCvc(event: any) {
        this.setState({ cvc: event.target.value });
    }

    // Ouvre la boite de dialogue de paiement
    private handlePayment = (vehicleMat: string,vehicleMark: string,vehicleDelivery: string,vehiclePrice: number, index: number) => () => {
        this.setState({ matVehicle: vehicleMat, mark: vehicleMark, delivery: vehicleDelivery,price: vehiclePrice, vehicleIndex: index, openPayment: true });
    }

    // Ferme la boite de dialogue de paiement
    private handleClose() {
        this.setState({ openPayment: false });
    }

    // Confirme l'ajoute au panier
    private handleConfirmAddToBasket = (mat: string) => () => {
        this.setState({ matVehicle: mat, addToBasket: !this.state.addToBasket });
    }

    // Récupère les achats
    private viewPurchases(){
        const GET_PURCHASES_URL: string = GET_PURCHASES + "?mail=" + this.state.userLogin + "&password=" + this.state.userPassword + "&currency=" + this.state.name;
        axios.get(GET_PURCHASES_URL).then(response => {
            this.setState({ openPurchase: !this.state.openPurchase, purchases: response.data, openOrCloseBasket: false});
        }).catch(error => {
            console.log(error);
        });
    }

    // Ferme le panier
    private handleOpenOrCloseBasket = (email: string, passWord: string) => () => {
        const GET_MY_BASKET_URL: string = BASKET_LIST + "?mail=" + email + "&password=" + passWord + "&currency=" + this.state.name;
        axios.get(GET_MY_BASKET_URL).then(response => {
            this.setState({ openOrCloseBasket: !this.state.openOrCloseBasket, myBasket: response.data });
        }).catch(error => {
            console.log(error);
        });
    }

    // Ajoute au panier
    private addInMyBasket = (email: string, passWord: string, vehicleMat: string) => () => {
        axios.post(ADD_TO_BASKET, { mail: email, password: passWord, matVehicle: vehicleMat }).then(response => {
            this.setState({ myBasket: response.data, addToBasket: false });
        }).catch(error => {
            console.log(error);
        });
    }

    // Ferme la boite de dialogue après le paiement
    private closeCardNumberAndCvCDialog() {
        if (this.state.openOrCloseBasket) {
            this.state.myBasket.splice(this.state.vehicleIndex, 1);
        }

        this.setState({ checkCardAndCvc: false });
    }

    // Récupère le login
    private handleChangeInputLogin(event: any) {
        this.setState({ userLogin: event.target.value });
    }

    // Récupère le mot de passe
    private handleChangeInputPassword(event: any) {
        this.setState({ userPassword: event.target.value });
    }

    // Gère la connexion
    private connexion() {
        const log = this.state.userLogin;
        const pass = this.state.userPassword;
        if (log === "" || pass === "") {
            return;
        }

        axios.post(CONNEXION, { login: log, password: pass }).then(response => {
            if (response.data === "ok") {
                this.home(log, pass);
                this.setState({ checkConnexion: true });
            }
        }).catch(error => {
            console.log(error);
        });
    }

    // Achète un véhicule
    private buyVehicle = (vehicleMat: string, index: number) => () => {
        const inputs: any = document.getElementsByTagName("Input");
        for (const input of inputs) {
            if (input.id === "") {
                this.setState({ purchaseAnswer: "Veuillez renseigner votre catre de crédit et cryptogramme !", checkCardAndCvc: true });
                return;
            }
        }

        axios.post(PURCHASE, { cardNumber: inputs[1].id, cvc: inputs[2].id, matVehicle: vehicleMat }).then(response => {
            if (response.data !== "Carte non valide!" && response.data !== "Solde insuffisant" && response.data !== "Véhicule déjà vendu") {
                this.state.vehicles.splice(index, 1);
            }
            this.setState({ purchaseAnswer: response.data, vehicles: this.state.vehicles, openPayment: false, checkCardAndCvc: true });
        }).catch(error => {
            console.log(error);
        });
    }

    // Met à jour la liste des véhicules après achat depuis le panier
    private updateListVehicle() {
        const HOME_URL = HOME + "?currency=" + this.state.name;
        axios.get(HOME_URL).then(res => {
            const allVehicleAndCurrencies = res.data;
            this.setState({ vehicles: allVehicleAndCurrencies[0], currencies:allVehicleAndCurrencies[1]});
        }).catch(error => {
            console.log(error);
        });
    }

    private deconnect(){
        this.setState({userLogin: '', userPassword: '', checkConnexion: false});
    }
});
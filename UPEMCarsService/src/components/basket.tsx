import { withStyles, WithStyles } from '@material-ui/core/styles';

import { PURCHASE } from "./appUrl";

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import InputBase from '@material-ui/core/InputBase';
import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Tooltip from '@material-ui/core/Tooltip';
import Typography from '@material-ui/core/Typography';

import axios from 'axios';
import * as React from 'react';

const decoratedBasket = withStyles(theme => ({
    button: {
        backgroundColor: '#EA5A0B',
        color: "#FFF",
        marginRight: '8%',
    },
    fab: {
        backgroundColor: '#000',
    },
    inputBase: {
        width: theme.spacing.unit * 50,
    },
    paper: {
        marginLeft: '10%',
        marginTop: -10,
        width: '80%'
    },
    resetButton: {
        backgroundColor: 'red',
        color: "#FFF",
        marginRight: '20%',
    },
    row: {
        '&:nth-of-type(odd)': {
            backgroundColor: theme.palette.background.default,
        },
    },
    tableCell: {
        color: theme.palette.common.white,
        fontSize: 14,
    },
    tableHead: {
        backgroundColor: '#EA5A0B',
    },
    typography: {
        backgroundColor: '#EA5A0B',
        color: "#FFF",
        fontSize: 20
    }
}));

interface IBasketProps {
    readonly data: any[];
    readonly currencyName: string,
    readonly updateListVehicle: () => void;
}

interface IBasketState {
    readonly carNumber: string;
    readonly checkCardAndCvc: boolean;
    readonly cvc: string;
    readonly delivery: string;
    readonly mark: string;
    readonly matVehicle: string;
    readonly openPayment: boolean;
    readonly price: number;
    readonly purchaseAnswer: string,
    readonly vehicleIndex: number;
}

export const BasketCustomized = decoratedBasket(class extends React.Component<IBasketProps & WithStyles<'button' | 'inputBase' | 'fab' | 'paper' | 'resetButton' | 'row' | 'tableHead' | 'tableCell' | 'typography'>, IBasketState>{

    constructor(props: any) {
        super(props);
        this.state = {
            carNumber: '',
            checkCardAndCvc: false,
            cvc: '',
            delivery: '',
            mark: '',
            matVehicle: '',
            openPayment: false,
            price: 0,
            purchaseAnswer: '',
            vehicleIndex: -1,
        },

        this.handlePayment = this.handlePayment.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.closeCardNumberAndCvCDialog = this.closeCardNumberAndCvCDialog.bind(this);
        this.handleChangeInputCard = this.handleChangeInputCard.bind(this);
        this.handleChangeInputCvc = this.handleChangeInputCvc.bind(this);
    }

    public render() {
        const { classes, data, currencyName} = this.props;
        return (
            <div>
                {this.state.checkCardAndCvc ?

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

                    <Dialog
                        aria-labelledby="simple-modal-title"
                        aria-describedby="simple-modal-description"
                        open={this.state.openPayment}
                        onClose={this.handleClose}
                    >
                        <DialogContent>
                            <Typography className={classes.typography}> {"Matricule : " + this.state.matVehicle}  <br />
                                {"Marque : " + this.state.mark} <br /> {"Livraison : " + this.state.delivery} <br /> {" Prix : " + this.state.price}</Typography> <br />
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
                }

                <Paper className={classes.paper} elevation={8} square={true}>
                    <Typography> Votre panier contient {data.length} véhicule(s)</Typography>
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
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {data.map((vehicle, index) => {
                                return (
                                    <TableRow className={classes.row} key={index}>
                                        <TableCell component="th" scope="row">{vehicle.kind}</TableCell>
                                        <TableCell >{vehicle.mat}</TableCell>
                                        <TableCell >{vehicle.mark}</TableCell>
                                        <TableCell >{vehicle.fuel}</TableCell>
                                        <TableCell >{vehicle.circulated}</TableCell>
                                        <TableCell >{vehicle.delivery}</TableCell>
                                        <TableCell >{vehicle.price}</TableCell>
                                        <TableCell >
                                            <Tooltip title="Acheter">
                                                <Button variant="fab" color="primary" className={classes.fab} onClick={this.handlePayment(vehicle.mat, vehicle.mark, vehicle.delivery, vehicle.price, index)}>
                                                    {currencyName}
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
            </div>
        )
    }

    private handleChangeInputCard(event: any) {
        this.setState({ carNumber: event.target.value });
    }

    private handleChangeInputCvc(event: any) {
        this.setState({ cvc: event.target.value });
    }

    private handlePayment = (vehicleMat: string, vehicleMark: string, vehicleDelivery: string, vehiclePrice: number, index: number) => () => {
        this.setState({ matVehicle: vehicleMat, mark: vehicleMark, delivery: vehicleDelivery, price: vehiclePrice, vehicleIndex: index, openPayment: true });
    }

    private handleClose() {
        this.setState({ openPayment: false });
    }


    private closeCardNumberAndCvCDialog() {
        this.setState({ checkCardAndCvc: false });
    }

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
                this.props.data.splice(index, 1);
                this.props.updateListVehicle();
            }
            this.setState({ purchaseAnswer: response.data, openPayment: false, checkCardAndCvc: true });
        }).catch(error => {
            console.log(error);
        });
    }
});
